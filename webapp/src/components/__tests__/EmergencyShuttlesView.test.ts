import { describe, it, expect, vi } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import axios from 'axios'
import EmergencyShuttlesView from '../EmergencyShuttlesView.vue'

vi.mock('axios')

describe('EmergencyShuttlesView', () => {
  it("n'affiche aucune navette par défaut", () => {
    const wrapper = mount(EmergencyShuttlesView)

    expect(wrapper.findAll('[data-cy="emergency-shuttle"]')).toHaveLength(0)
  })

  it("le bouton n'est pas rouge par défaut", () => {
    const wrapper = mount(EmergencyShuttlesView)

    expect(wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]').classes()).not.toContain('red-button')
  })

  it('le bouton devient rouge si le manifeste obtenu est vide', async () => {
    vi.mocked(axios.get).mockResolvedValueOnce({ data: { cost: 0, emergencyShuttles: [] } })
    const wrapper = mount(EmergencyShuttlesView)

    await wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]').trigger('click')
    await flushPromises()

    expect(wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]').classes()).toContain('red-button')
  })

  it("affiche les navettes dans l'ordre reçu", async () => {
    const shuttles = [
      { type: 'RESCUE_SHIP', travelers: [], crewMembers: [] },
      { type: 'STANDARD_SHUTTLE', travelers: [], crewMembers: [] },
    ]
    vi.mocked(axios.get).mockResolvedValueOnce({ data: { cost: 200000, emergencyShuttles: shuttles } })
    const wrapper = mount(EmergencyShuttlesView)

    await wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]').trigger('click')
    await flushPromises()

    const renderedShuttles = wrapper.findAll('[data-cy="emergency-shuttle"]')
    expect(renderedShuttles).toHaveLength(2)
    expect(renderedShuttles[0].text()).toContain('RESCUE_SHIP')
    expect(renderedShuttles[1].text()).toContain('STANDARD_SHUTTLE')
  })

  it('affiche le coût total du manifeste', async () => {
    const shuttles = [{ type: 'RESCUE_SHIP', travelers: [], crewMembers: [] }]
    vi.mocked(axios.get).mockResolvedValueOnce({ data: { cost: 150000, emergencyShuttles: shuttles } })
    const wrapper = mount(EmergencyShuttlesView)

    await wrapper.get('[data-cy="fetch-emergency-shuttles-manifest"]').trigger('click')
    await flushPromises()

    expect(wrapper.get('[data-cy="total-cost"]').text()).toBe('150000')
  })
})
